import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PickShareFunctional {
    static Optional<ShareInfo> findHighPrices(Stream<String> shares) {
        final Predicate<? super ShareInfo> isPriceLessThan500 = ShareUtil.isPriceLessThan(500);

        AtomicInteger index = new AtomicInteger();

        return shares.map(symbol -> {
            if (index.get() != 0 && index.get() % 5 == 0) {
                try {
                    System.out.println("Sleeping thread for limiter ...");
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            index.getAndIncrement();

            ShareInfo price = ShareUtil.getPrice(symbol);
            System.out.println(price);

            return price;
        }).filter(isPriceLessThan500).max(Comparator.comparing(s -> s.price));
    }

    public static void main(String[] args) {

        long startTime = System.nanoTime();
        Optional<ShareInfo> max = PickShareFunctional.findHighPrices(Shares.symbols.parallelStream());
        long stopTime = System.nanoTime();
        System.out.println((stopTime - startTime) / 1000000 + " Milliseconds");

        if (max.isPresent())
            System.out.println("High priced under $500 is " + max.get());
        else
            System.out.println("None high priced under $500");

        Thread.sleep(60000);

        long startTime2 = System.nanoTime();
        Optional<ShareInfo> max2 = PickShareFunctional.findHighPrices(Shares.symbols.stream());
        long stopTime2 = System.nanoTime();
        System.out.println((stopTime2 - startTime2) / 1000000 + "Milliseconds");

        if (max2.isPresent())
            System.out.println("High priced under $500 is " + max2.get());
        else
            System.out.println("None high priced under $500");
//        long startTime = System.nanoTime();
//        final Predicate<ShareInfo> isPriceLessThan500 = ShareUtil.isPriceLessThan(500);
//        ShareInfo highPriced = new ShareInfo("", new BigDecimal(-1));
//        for(String symbol : Shares.symbols) {
//            ShareInfo shareInfo = ShareUtil.getPrice(symbol);
//            if(isPriceLessThan500.test(shareInfo))
//                highPriced = ShareUtil.pickHigh(highPriced, shareInfo);
//        }
//        long stopTime = System.nanoTime();
//        System.out.println(stopTime - startTime);
//        System.out.println("High priced under $500 is " + highPriced);
    }
}
