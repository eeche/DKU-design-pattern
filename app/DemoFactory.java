package app;

import domain.Region;
import pricing.factory.PricePolicyFactory;
import pricing.factory.RegionPricePolicyFactory;
import pricing.policy.PricePolicy;

import java.math.BigDecimal;

public class DemoFactory {
    public static void main(String[] args) {
        PricePolicyFactory factory = new RegionPricePolicyFactory();
        BigDecimal base = new BigDecimal("100000");

        for (Region r : Region.values()) {
            PricePolicy p = factory.create(r);
            BigDecimal after = p.apply(base);
            System.out.printf("[%s] base=%,.0f → %, .0f (%s)%n",
                    r, base.doubleValue(), after.doubleValue(), p.name());
        }
    }
}
