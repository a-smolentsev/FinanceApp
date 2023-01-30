/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:38 AM
 * copyright (c) 2020
 * last modified 5/14/20 5:30 AM with ❤
 */

package com.add.finance.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CategoryStat {
    @Getter
    private String category;
    @Getter
    private Double total;
}
