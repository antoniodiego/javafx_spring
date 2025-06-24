/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antoniodiego.javafxspring.model;

/**
 *
 * @author Antônoio Diego <antoniodiegoluz at gmail.com>
 */
public enum TipoVoto {
    Lembrei("lembrei"),
    Proclastinei("proclastinei");

    private final String codigo;

    private TipoVoto(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

}
