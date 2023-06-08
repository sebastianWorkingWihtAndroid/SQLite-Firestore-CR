package com.example.adopta.entidades;

public class Visitas {
    private String fechaViewFecha;
    private String visitaViewVisita;
    private String cedulaViewCedula;
    private String objetoViewObjeto;
    private String firma;
    private String observacionesViewObservaciones;


    public String getFechaViewFecha() {
        return fechaViewFecha;
    }

    public void setFechaViewFecha(String fechaViewFecha) {
        this.fechaViewFecha = fechaViewFecha;
    }

    public String getVisitaViewVisita() {
        return visitaViewVisita;
    }

    public void setVisitaViewVisita(String visitaViewVisita) {
        this.visitaViewVisita = visitaViewVisita;
    }

    public String getCedulaViewCedula() {
        return cedulaViewCedula;
    }

    public void setCedulaViewCedula(String cedulaViewCedula) {
        this.cedulaViewCedula = cedulaViewCedula;
    }

    public String getObjetoViewObjeto() {
        return objetoViewObjeto;
    }

    public void setObjetoViewObjeto(String objetoViewObjeto) {
        this.objetoViewObjeto = objetoViewObjeto;
    }

    public String getObservacionesViewObservaciones() {
        return observacionesViewObservaciones;
    }

    public void setObservacionesViewObservaciones(String observacionesViewObservaciones) {
        this.observacionesViewObservaciones = observacionesViewObservaciones;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
}
