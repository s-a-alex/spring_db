package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@NamedQueries({
		@NamedQuery(name="singer.findById", query="select distinct s from Singer s left join fetch s.albums a left join fetch s.instruments i where s.id = :id"),
		@NamedQuery(name="singer.findAllWithAlbum", query="select distinct s from Singer s left join fetch s.albums a left join fetch s.instruments i")
})
@SqlResultSetMapping(name="singerResult", entities=@EntityResult(entityClass=Singer.class))
public class Singer implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	@Version
	private int version;
	@OneToMany(mappedBy = "singer", cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<Album> albums = new HashSet<>();
	@ManyToMany
	@JoinTable(name = "singer_instrument", joinColumns = @JoinColumn(name = "SINGER_ID"), inverseJoinColumns = @JoinColumn(name = "INSTRUMENT_ID"))
	private Set<Instrument> instruments = new HashSet<>();

	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return this.id;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getVersion() {
		return version;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}
	public Set<Album> getAlbums() {
		return albums;
	}
	public void setInstruments(Set<Instrument> instruments) {
		this.instruments = instruments;
	}
	public Set<Instrument> getInstruments() {
		return instruments;
	}
	public boolean addAlbum(Album album) {
		album.setSinger(this);
		return getAlbums().add(album);
	}
	@Override
	public String toString() {
		return String.format("Singer - id: %d, First name: %s, Last name: %s, Birthday: %s, Version: %d", id, firstName, lastName, new SimpleDateFormat("yyyy-MM-dd").format(birthDate), version);
	}
	public boolean equals(Object o) {
		try{
			Singer singer = (Singer) o;
			if(id.equals(singer.id)&&firstName.equals(singer.firstName)&&lastName.equals(singer.lastName)&&birthDate.equals(singer.birthDate)) return true;
		} catch(Exception e){}
		return false;
	}
	public int hashCode() {
		int result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
		return result;
	}
}
