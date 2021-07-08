package com.oup.eac.web.tags;

import java.util.Set;

/**
 * A custom function to perform contains with a set and value.
 * 
 * @author David Hay
 * 
 */
public class ContainsFn {
    
     public static boolean contains(Set<?> items, Object item){   //notice that this method doesn't have to be  the same as the one that will apear in your tag 'randomColor'. This is only true for a class that implements a Custom El Function.
        boolean result = false;

        if (item != null && items != null) {
            result = items.contains(item);
        }
         return result;
    }

}
