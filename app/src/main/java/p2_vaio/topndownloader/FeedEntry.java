package p2_vaio.topndownloader;

/**
 * Created by p2_vaio on 7/1/2017.
 */

public class FeedEntry {
    private String title;
    private String summary;
    private String duration;
    private String imageURL;
    private String releasedate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    @Override
    public String toString() {
        return "title='" + title + "\n" +
                " summary='" + summary + "\n" +
                " duration='" + duration + "\n" +
                " imageURL='" + imageURL + "\n" +
                " releasedate='" + releasedate + "\n" +
                '}';
    }
}
