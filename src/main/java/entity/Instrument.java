package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Instrument implements Serializable {
	@Id
	@Column(name = "ID")
	private String instrumentId;
	@ManyToMany
	@JoinTable(name = "singer_instrument", joinColumns = @JoinColumn(name = "INSTRUMENT_ID"), inverseJoinColumns = @JoinColumn(name = "SINGER_ID"))
	private Set<Singer> singers = new HashSet<>();

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
	public String getInstrumentId() {
		return this.instrumentId;
	}
	public void setSingers(Set<Singer> singers) {
		this.singers = singers;
	}
	public Set<Singer> getSingers() {
		return this.singers;
	}
	@Override
	public String toString() {
		return "Instrument :" + getInstrumentId();
	}
}