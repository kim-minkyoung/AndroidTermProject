import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class MusicResponse(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

data class Channel(
    @field:Element(name = "title")
    var title: String? = null,
    @field:Element(name = "link", required = false)
    var link: String? = null,
    @field:Element(name = "description", required = false)
    var description: String? = null,
    @field:Element(name = "lastBuildDate", required = false)
    var lastBuildDate: String? = null,
    @field:Element(name = "total", required = false)
    var total: Int = 0,
    @field:Element(name = "start", required = false)
    var start: Int = 0,
    @field:Element(name = "display", required = false)
    var display: Int = 0,
    @field:Element(name = "urlbase", required = false)
    var urlBase: String? = null,
    @field:ElementList(inline = true, entry = "item", required = false)
    var items: List<Item>? = null,



)

data class Item(
    @field:Attribute(name = "id", required = false)
    var id: String? = null,
    @field:Element(name = "title", required = false)
    var title: String? = null,
    @field:Element(name = "runningtime", required = false)
    var runningtime: String? = null,
    @field:Element(name = "link", required = false)
    var link: String? = null,
    @field:Element(name = "pubDate", required = false)
    var pubDate: String? = null,
    @field:Element(name = "author", required = false)
    var author: String? = null,
    @field:Element(name = "description", required = false)
    var description: String? = null,
    @field:Element(name = "guid", required = false)
    var guid: String? = null,
    @field:Element(name = "comments", required = false)
    var comments: String? = null,
    @field:Element(name = "album", required = false)
    var album: Album? = null,
    @field:Element(name = "artist", required = false)
    var artist: Artist? = null,
    @field:Element(name = "trackartists", required = false)
    var trackArtists: TrackArtists? = null,
    @field:Element(name = "trackartistlist", required = false)
    var trackArtistList: TrackLists? = null
)

data class Album(
    @field:Element(name = "title", required = false)
    var title: String? = null,
    @field:Element(name = "release", required = false)
    var release: String? = null,
    @field:Element(name = "link", required = false)
    var link: String? = null,
    @field:Element(name = "image", required = false)
    var image: String? = null,
    @field:Element(name = "description", required = false)
    var description: String? = null
)

data class Artist(
    @field:Attribute(name = "status", required = false)
    var status: String? = null,
    @field:Element(name = "link", required = false)
    var link: String? = null,
    @field:Element(name = "name", required = false)
    var name: String? = null
)

data class TrackArtists(
    @field:ElementList(inline = true, entry = "artist", required = false)
    var artists: List<TrackArtist>? = null
)

data class TrackArtist(
    @field:Attribute(name = "id", required = false)
    var id: String? = null,
    @field:Element(name = "id", required = false)
    var ID: Int? = 0,
    @field:Element(name = "name", required = false)
    var name: String? = null
)
data class TrackLists(
    @field:Attribute(name = "status", required = false)
    var status: String? = null,
    @field:ElementList(inline = true, entry = "trackartist", required = false)
    var trackArtists: String? = null
)
