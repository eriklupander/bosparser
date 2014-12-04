package se.lu.bos.rest.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-04
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class TinyReport {
    private Long id;
    private String title;
    private String created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if(title != null) {
            if(title.endsWith("_gen.msnbin")) {
                this.title = "Quick Mission";
            } else {
                this.title = title.substring(title.lastIndexOf("/")+1, title.length() - 7);
            }
        }
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
