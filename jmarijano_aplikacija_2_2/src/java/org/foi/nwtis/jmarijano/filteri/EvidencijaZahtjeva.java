package org.foi.nwtis.jmarijano.filteri;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.eb.Dnevnik;
import org.foi.nwtis.jmarijano.sb.DnevnikFacade;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;

@WebFilter(filterName = "EvidencijaZahtjeva", urlPatterns = {"/*"})
public class EvidencijaZahtjeva implements Filter {

    @EJB
    private DnevnikFacade dnevnikFacade;
    @EJB
    private StatefulSBApp2_1 statefulSB;
    private Dnevnik dnevnik;
    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public EvidencijaZahtjeva() {
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc)
            throws IOException, ServletException {
        long pocetak = System.currentTimeMillis();
        String korisnik = "nepoznat";
        HttpServletRequest request = (HttpServletRequest) sr;
        String contekstPutanja = (request.getRequestURI() != null && !"".equals(request.getRequestURI())) ? request.getContextPath() : "";
        String servletPutanja = (request.getServletPath() != null && !"".equals(request.getServletPath())) ? request.getServletPath() : "";
        String putanjaInfo = (request.getPathInfo() != null && !"".equals(request.getPathInfo())) ? request.getPathInfo() : "";
        String url = contekstPutanja + servletPutanja + putanjaInfo;
        String ipadresa = (request.getRemoteAddr() != null && !"".equals(request.getRemoteAddr())) ? request.getRemoteAddr() : "";
        HttpSession sesija = ((HttpServletRequest) request).getSession(false);
        if (sesija != null) {
            if (sesija.getAttribute("korisnik") != null) {
                korisnik = (String) sesija.getAttribute("korisnik");
            } else {
                statefulSB.zaustaviSlusaca();
            }
        } else {
            statefulSB.zaustaviSlusaca();
        }
        fc.doFilter(sr, sr1);
        long kraj = System.currentTimeMillis();
        if (!"".equals(url) && !korisnik.equals("nepoznat")) {
            dnevnik = new Dnevnik();
            dnevnik.setId(0);
            dnevnik.setIpAdresa(ipadresa);
            dnevnik.setKorisnik(korisnik);
            dnevnik.setTrajanje((int) (kraj - pocetak));
            dnevnik.setUrl(url);
            dnevnik.setVrijeme(new Timestamp(System.currentTimeMillis()));
            dnevnikFacade.create(dnevnik);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("EvidencijaZahtjeva:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("EvidencijaZahtjeva()");
        }
        StringBuffer sb = new StringBuffer("EvidencijaZahtjeva(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
