package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Album implements Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column
	private String title;
	@Temporal(TemporalType.DATE)
	private Date releaseDate;
	@Version
	private int version;
	@ManyToOne
	@JoinColumn(name = "SINGER_ID")
	private Singer singer;

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
	public void setSinger(Singer singer) {
		this.singer = singer;
	}
	public Singer getSinger() {
		return this.singer;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Date getReleaseDate() {
		return this.releaseDate;
	}
	@Override
	public String toString() {
		return String.format("Album - id: %d, Singer id: %d, Title: %s, Release Date: %s", id, singer.getId(), title, new SimpleDateFormat("yyyy-MM-dd").format(releaseDate));
	}
	public boolean equals(Object o) {
		try{
			Album album = (Album) o;
			if(id.equals(album.id)&&title.equals(album.title)&&releaseDate.equals(album.releaseDate)) return true;
		} catch(Exception e){}
		return false;
	}
	public int hashCode() {
		int result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (title != null ? title.hashCode() : 0);
		result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
		return result;
	}
}
