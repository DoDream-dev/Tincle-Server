package tinqle.tinqleServer.domain.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    NORMAL("NORMAL"),
    HAPPY("happy"),
    SAD("sad"),
    ANGRY("ANGRY"),
    EXHAUSTED("EXHAUSTED"),
    COFFEE("COFFEE"),
    EATING("EATING"),
    ALCOHOL("ALCOHOL"),
    CHICKEN("CHICKEN"),
    SLEEP("SLEEP"),
    WORKING("WORKING"),
    STUDYING("STUDYING"),
    MOVIE("MOVIE"),
    MOVING("MOVING"),
    BORING("BORING"),
    READ("READ"),
    SHOES("SHOES"),
    PLAIN("PLAIN");

    private final String statusImageUrl;


}
