/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd.conf;

/**
 *
 * @author mvineet
 */
class OSPFAuthentication {

    public enum OSPF_AUTHTYPE {
        PASSWORD, MD5
    };

    private OSPF_AUTHTYPE authType;
    private byte keyId;

    public OSPF_AUTHTYPE getAuthType() {
        return authType;
    }

    public byte getKeyId() {
        return keyId;
    }

    public void setAuthType(OSPF_AUTHTYPE authType) {
        this.authType = authType;
    }

    public void setKeyId(byte keyId) {
        this.keyId = keyId;
    }

    public OSPFAuthentication(OSPF_AUTHTYPE authType, byte keyId) {
        this.authType = authType;
        this.keyId = keyId;
    }
}
