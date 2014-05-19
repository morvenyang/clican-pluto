//
//  StylesSheet.h
//  hpsdf-ngp-mudole-iphone-client
//
//  Created by zhang wei on 11-11-22.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "StyleSheet.h"

@implementation StyleSheet

- (TTStyle*)configLabel {
    TTStyle* style = [TTTextStyle styleWithFont: [UIFont boldSystemFontOfSize:18]
                         color: [UIColor blackColor]
                          next: nil];
    return style;
}

+(UIColor*) colorFromHexString:(NSString*)hexString{
    unsigned rgbValue = 0;
    NSScanner* scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1];
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue&0xFF0000)>>16)/255.0 green:((rgbValue&0xFF00)>>8)/255.0 blue:(rgbValue&0xFF)/255.0 alpha:1.0];
}
@end