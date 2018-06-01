package com.cadit.cqrs.cdi;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lettura da configurazione dell'applicazione
 * singleton inizializzato a lazy
 */
@ApplicationScoped
public class Configuration  {


    private Properties conf = null;


    public Configuration() {
        InputStream is = Configuration.class.getResourceAsStream("/application-module.properties");
        conf = loadProperties(is);
    }


    public Properties getConf() {
        return conf;
    }


    private Properties loadProperties(InputStream is) {
        Properties props = new Properties();
        try {
            if (is != null) {
                props.load(is);
            } else {
                props = null;
            }
        } catch (IOException ex) {
            System.err.println("Errore nella lettura della configurazione (" + ex.getMessage() + ")");
            props = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    System.err.println("Errore nella lettura della configurazione (" + ioe.getMessage() + ")");
                }
            }
        }
        return props;
    }



}
