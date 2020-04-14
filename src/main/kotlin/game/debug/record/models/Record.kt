package game.debug.record.models

class Record() {
    private val stepsChoice : ArrayList<String> = ArrayList()
    public var description: String = "none"
    public var id: String= "none"

    public constructor(id: String, description: String) : this() {
        this.id = id;
        this.description = description;
    }

    public constructor(id: String) : this() {
        this.id = id;
    }

    public fun addNextStep(id: String){
        stepsChoice.add(id);
    }
    public fun toArray() : Array<String> { return  stepsChoice.toTypedArray()};

}