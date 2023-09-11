package tinqle.tinqleServer.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static tinqle.tinqleServer.common.constant.GlobalConstants.ACCOUNT_STATUS_URL;

@AllArgsConstructor
@Getter
public enum Status {
    SMILE(ACCOUNT_STATUS_URL+"/status/smile.png"),
    HAPPY(ACCOUNT_STATUS_URL+"/status/happy.png"),
    SAD(ACCOUNT_STATUS_URL+"/status/sad.png"),
    MAD(ACCOUNT_STATUS_URL+"/status/mad.png"),
    EXHAUSTED(ACCOUNT_STATUS_URL+"/status/exhausted.png"),
    CHICKEN(ACCOUNT_STATUS_URL+"/status/chicken.png"),
    ALCOHOL(ACCOUNT_STATUS_URL+"/status/alcohol.png"),
    COFFEE(ACCOUNT_STATUS_URL+"/status/coffee.png"),
    READ(ACCOUNT_STATUS_URL+"/status/read.png"),
    WORK(ACCOUNT_STATUS_URL+"/status/work.png"),
    MEAL(ACCOUNT_STATUS_URL+"/status/meal.png"),
    MOVIE(ACCOUNT_STATUS_URL+"/status/movie.png"),
    SLEEP(ACCOUNT_STATUS_URL+"/status/sleep.png"),
    TRAVEL(ACCOUNT_STATUS_URL+"/status/travel.png"),
    STUDY(ACCOUNT_STATUS_URL+"/status/study.png"),
    WALK(ACCOUNT_STATUS_URL+"/status/walk.png");

    private final String statusImageUrl;

}
