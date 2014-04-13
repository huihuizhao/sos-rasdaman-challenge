package org.n52.gsoc.challenge;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ByteArrayConverter implements Converter {

	@Override
	public boolean canConvert(Class c) {
		return c.equals(RasImg.class);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext arg2) {
		RasImg rasImg = (RasImg) value;
		writer.startNode("rasimg");
		writer.setValue(rasImg.toString());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext arg1) {
		RasImg rasImg = new RasImg();
		reader.moveDown();
		String[] values = reader.getValue().split(", ");
		byte[] vals = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			vals[i] = (byte) Integer.parseInt(values[i]);
		}
		rasImg.setValues(vals);
		reader.moveUp();
		return rasImg;
	}

}
