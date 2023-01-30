/*
 * created by Andrew K. <rembozebest@gmail.com> on 5/14/20 5:38 AM
 * copyright (c) 2020
 * last modified 5/14/20 2:33 AM with ❤
 */

package com.add.finance.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class FinanceOperation {
    @Getter
    private String account;
    @Getter
    private String category;
    @Getter
    private String dealDate;
    @Getter
    private String description;
    @Getter
    private Double total;
}
