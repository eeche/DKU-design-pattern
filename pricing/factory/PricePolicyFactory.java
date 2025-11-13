package pricing.factory;

import domain.Region;
import pricing.policy.PricePolicy;

public abstract class PricePolicyFactory {
    // Factory Method – 지역별 가격정책 객체 생성
    public abstract PricePolicy create(Region region);

    // 잘못된 입력 처리 통일(원한다면 공통 전처리/후처리 가능)
    protected void validate(Region region) {
        if (region == null) throw new IllegalArgumentException("Region is required.");
    }
}
