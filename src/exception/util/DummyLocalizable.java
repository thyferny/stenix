package exception.util;

import java.util.Locale;

public class DummyLocalizable implements Localizable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 8843275624471387299L;

    /** Source string. */
    private final String source;

    /** Simple constructor.
     * @param source source text
     */
    public DummyLocalizable(final String source) {
        this.source = source;
    }

    /** {@inheritDoc} */
    public String getSourceString() {
        return source;
    }

    /** {@inheritDoc} */
    public String getLocalizedString(Locale locale) {
        return source;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return source;
    }

}
