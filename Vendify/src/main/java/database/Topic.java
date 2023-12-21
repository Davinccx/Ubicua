
package database;


public class Topic {
    
    private int saldo;
    private String value;

    public Topic() {
        this.saldo = 0;
        this.setValue(null);
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
    
}
