package pricing.factory;

import domain.Region;
import pricing.policy.BusanPricePolicy;
import pricing.policy.JejuPricePolicy;
import pricing.policy.PricePolicy;
import pricing.policy.SeoulPricePolicy;

public class RegionPricePolicyFactory extends PricePolicyFactory {
    @Override
    public PricePolicy create(Region region) {
        validate(region);
        switch (region) {
            case SEOUL: return new SeoulPricePolicy();
            case BUSAN: return new BusanPricePolicy();
            case JEJU:  return new JejuPricePolicy();
            default: throw new UnsupportedOperationException("Unsupported region: " + region);
        }
    }
}
