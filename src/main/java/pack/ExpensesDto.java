package pack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExpensesDto {
    @NotNull
    private String name;
    @NotNull
    private String expensesKind;
    private String description;
    @NotNull
    private Long cost;
    private boolean paymentType;
    @NotNull
    private Timestamp created;
}
