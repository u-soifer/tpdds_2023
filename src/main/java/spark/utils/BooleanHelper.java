package spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

public enum BooleanHelper implements Helper<Boolean>{

    isTrue{
        @Override
        public CharSequence apply(Boolean arg0, Options arg1) {
            if (arg0)
                return "checked";
            else
                return "notchecked";
        }
    }

}