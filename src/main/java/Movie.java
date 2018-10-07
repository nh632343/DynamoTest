import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "movies")
public class Movie {

    private Long year;


    private String title;

    private String one;

    private String two;

    private String three;

    @DynamoDBHashKey
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    @DynamoDBRangeKey
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute
    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    @DynamoDBAttribute
    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    @DynamoDBAttribute
    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "year=" + year +
                ", title='" + title + '\'' +
                ", one='" + one + '\'' +
                ", two='" + two + '\'' +
                ", three='" + three + '\'' +
                '}';
    }
}
