package kr.aling.user.companyuser.type;

import lombok.Getter;

/**
 * The enum for managing Company Size.
 *
 * @author : 여운석
 * @since : 1.0
 **/
@Getter
public enum CompanySize {
    CORPORATE("대기업"),
    MEDIUM("중견기업"),
    SMALL("중소기업");

    private final String value;

    CompanySize(String value) {
        this.value = value;
    }
}
