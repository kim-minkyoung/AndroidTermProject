import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class MusicResponse(
    @field:Element(name = "channel")
    var channel: Channel? = null
)

data class Channel(
    @field:Element(name = "item")
    var item: Item? = null
)

data class Item(
    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "description")
    var description: String? = null,

    @field:Element(name = "maniadb:album")
    var album: Album? = null,

    @field:Element(name = "maniadb:artist")
    var artist: Artist? = null
)

data class Album(
    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "image")
    var image: String? = null
)

data class Artist(
    @field:Element(name = "name")
    var name: String? = null
)
