import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;
import java.util.Timer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PickShareFunctional {
    static ShareInfo findHighPrices(Stream<String> shares) {
        final Predicate isPriceLessThan500 = ShareUtil.isPriceLessThan(500);
        Optional result = shares.map(ShareUtil::getPrice)
                .filter(isPriceLessThan500)
                .max(Comparator.comparing(s -> ((ShareInfo)s).price));
        return (ShareInfo) result.get();
    }

    public static void main(String[] args) {

//        long startTime = System.nanoTime();
//        ShareInfo max = PickShareFunctional.findHighPrices(Shares.symbols.parallelStream());
//        long stopTime = System.nanoTime();
//        System.out.println(stopTime - startTime);
//        System.out.println("High priced under $500 is " + max);

        long startTime = System.nanoTime();
        final Predicate<ShareInfo> isPriceLessThan500 = ShareUtil.isPriceLessThan(500);
        ShareInfo highPriced = new ShareInfo("", new BigDecimal(-1));
        for(String symbol : Shares.symbols) {
            ShareInfo shareInfo = ShareUtil.getPrice(symbol);
            if(isPriceLessThan500.test(shareInfo))
                highPriced = ShareUtil.pickHigh(highPriced, shareInfo);
        }
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
        System.out.println("High priced under $500 is " + highPriced);
    }
}
