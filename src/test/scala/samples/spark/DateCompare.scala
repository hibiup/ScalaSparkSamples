package samples.spark

object DateCompare {
    implicit class StringToDate(stringDate: String ) {
        def date = parserString(stringDate)

        def parserString(stringDate:String):java.util.Date = {
            new java.util.Date()  // Replace by you way to parser the string
        }
    }

    implicit class comparableDate(date: java.util.Date) {
        def <(that:java.util.Date) ={true}  // Replace by your way to compare the date
        def >(that:java.util.Date) ={true}  // Replace by your way to compare the date
        def >=(that:java.util.Date) ={true}  // Replace by your way to compare the date
        // TODO:...
    }

    def main(args:Array[String]) = {
        assert("string".date < "another_string".date)
        assert("string".date > "another_string".date)
        assert("string".date >= "another_string".date)
    }
    // ...
}
