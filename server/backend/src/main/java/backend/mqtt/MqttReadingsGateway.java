package backend.mqtt;

public interface MqttReadingsGateway {
    /*
     TODO: MqttReadingsAdapter i Gateway napraviti da se pretplate na '+/up/#' topic,
      tako će dobivati sve podatke na up kanalu korisnika. Po primitku poruke na tom kanalu
      uzima se korisnik naveden u topicu prije /up/# i gledaju se njegovi uređaji i subtopici
      uređaja odgovaraju li topicu primljene poruke, ako odgovaraju potrebno ih je parsirati
      po načinu navedenom u topicu i spremiti za Reading pod taj topic. MqttReadingsGateway će trebati
      primati REST zahtjev za slanje poruka na pojedine topice i rukovati s njima.
     */

}
