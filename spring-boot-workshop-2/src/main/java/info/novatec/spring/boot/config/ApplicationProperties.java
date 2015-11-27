package info.novatec.spring.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Configuration properties for the application.
 */
@ConfigurationProperties(prefix = "myapp")
public class ApplicationProperties {

    /**
     * Flag to show or hide the current date on the web page.
     */
    @NotNull
    private Boolean showCurrentDate;

    /**
     * Show the current date on the web page.
     * @return if current date should be shown
     */
    public boolean isShowCurrentDate () {
        return showCurrentDate;
    }

    /**
     * Show the current date on the web page.
     * @param showCurrentDate if current date should be shown
     */
    public void setShowCurrentDate ( boolean showCurrentDate ) {
        this.showCurrentDate = showCurrentDate;
    }
}
