package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ParametresHandler.ParametresHandler;

import static org.junit.jupiter.api.Assertions.*;

class ParametresHandlerTest {
    public ParametresHandler handler;
    
    @BeforeEach
    void initHandler_creeNouvelleInstance() {
        handler = new ParametresHandler() {
            private Double param1;
            private Integer param2;
            private Float param3;
            private Byte param4;

            @SuppressWarnings("unused")
			public Double getParam1() {
                return param1;
            }

            @SuppressWarnings("unused")
			public void setParam1(Double param1) {
                this.param1 = param1;
            }

            @SuppressWarnings("unused")
			public Integer getParam2() {
                return param2;
            }

            @SuppressWarnings("unused")
			public void setParam2(Integer param2) {
                this.param2 = param2;
            }

            @SuppressWarnings("unused")
			public Float getParam3() {
                return param3;
            }

            @SuppressWarnings("unused")
			public void setParam3(Float param3) {
                this.param3 = param3;
            }

            @SuppressWarnings("unused")
			public Byte getParam4() {
                return param4;
            }

            @SuppressWarnings("unused")
			public void setParam4(Byte param4) {
                this.param4 = param4;
            }
        };
    }
    

    @Test
    void initialiseParametresRepr_quatreSettersDetectes_listeContientLesQuatreParametres() {
        assertTrue(handler.getNomsParametres().contains("Param1"), "Param1 devrait être détecté.");
        assertTrue(handler.getNomsParametres().contains("Param2"), "Param2 devrait être détecté.");
        assertTrue(handler.getNomsParametres().contains("Param3"), "Param3 devrait être détecté.");
        assertTrue(handler.getNomsParametres().contains("Param4"), "Param4 devrait être détecté.");
    }
    
    @Test
    void initialiseParametresRepr_classDefinitTypePrimitif_echoue() {
        // Tester une sous-classe mal définie où les setters ne suivent pas la convention de ne pas avoir de type primitife
        Exception exception = assertThrows(RuntimeException.class, () -> {
            @SuppressWarnings("unused")
			ParametresHandler badHandler = new ParametresHandler() {
                private int primitiveParameter;

                @SuppressWarnings("unused")
				public void setPrimitiveParameter(int param) {
                    this.primitiveParameter = param;
                }

                public int getPrimitiveParameter() {
                    return primitiveParameter;
                }
            };
        });
        
        assertTrue(exception.getMessage().contains("Les types primitifs ne sont pas autorisés : PrimitiveParameter"), 
            "Le message d'erreur devrait indiquer que la classe est mal définie.");
    }

    @Test
    void setValeurParametre_valeursCorrectes_parametresMisAJour() {
        handler.setValeurParametre("Param1", 123.45);
        handler.setValeurParametre("Param2", 42);
        handler.setValeurParametre("Param3", 12.34f);
        handler.setValeurParametre("Param4", Byte.valueOf((byte) 7));
        

        assertEquals(123.45,   handler.getValeurParametre("Param1"), "La valeur de Param1 devrait être mise à jour.");
        assertEquals(42,       handler.getValeurParametre("Param2"), "La valeur de Param2 devrait être mise à jour.");
        assertEquals(12.34f,   handler.getValeurParametre("Param3"), "La valeur de Param3 devrait être mise à jour.");
        assertEquals(Byte.valueOf((byte) 7), handler.getValeurParametre("Param4"), "La valeur de Param4 devrait être mise à jour.");
        
    }

    @Test
    void setValeurParametre_typeIncorrect_exceptionDeclenchee() {

    	Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.setValeurParametre("Param1", "str");
        });
        assertTrue(exception.getMessage().contains("Le type de la valeur ne correspond pas au type attendu pour : Param1"), 
            "Le message d'erreur devrait indiquer une incompatibilité de type.");
    }

    @Test
    void setValeurParametre_parametreInexistant_exceptionDeclenchee() {
        // Tester la tentative de définir une valeur sur un paramètre inexistant
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.setValeurParametre("UnknownParam", "value");
        });
        assertTrue(exception.getMessage().contains("Paramètre introuvable : UnknownParam"), 
            "Le message d'erreur devrait indiquer que le paramètre est introuvable.");
    }
}
