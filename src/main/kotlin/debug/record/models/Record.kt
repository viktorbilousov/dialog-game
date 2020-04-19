package debug.record.models

class Record {
    private val stepsChoice : ArrayList<String> = ArrayList()
    public var description: String = "none"
    public val id: String

    public constructor(id: String, description: String)  {
        this.id = id;
        this.description = description;
    }

    public constructor(id: String) {
        this.id = id;
    }

    public fun addNextStep(id: String){
        stepsChoice.add(id);
    }
    public fun toArray() : Array<String> { return  stepsChoice.toTypedArray()};

    override fun equals(other: Any?): Boolean {
        if(other !is Record) return false
        return other.id == id;
    }

    override fun hashCode(): Int {
        return id.hashCode() * 32
    }

    override fun toString(): String {
        return "{id: $id, description: $description, steps: ${stepsChoice.toTypedArray().contentToString()} }"
    }
}