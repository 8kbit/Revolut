package com.revolut.helper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.DateSerializer;

/**
 * Created by zaskanov on 18.09.2017.
 */
public class CustomDateSerializer extends DateSerializer {

  private final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");

  @Override
  public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException {
    jgen.writeString(df.format(value));
  }
}
