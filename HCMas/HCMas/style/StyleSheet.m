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
- (TTStyle*) settingRow1{
    TTStyle* style = [TTTextStyle styleWithFont: [UIFont boldSystemFontOfSize:16]
                                          color: [UIColor whiteColor]
                                           next: nil];
    return style;
}
- (TTStyle*) settingRow2{
    TTStyle* style = [TTTextStyle styleWithFont: [UIFont boldSystemFontOfSize:12]
                                          color: [UIColor blackColor]
                                           next: nil];
    return style;
}
- (TTStyle*) settingRow3{
    TTStyle* style = [TTTextStyle styleWithFont: [UIFont boldSystemFontOfSize:11]
                                          color: [UIColor whiteColor]
                                           next: nil];
    return style;
}
- (TTStyle*) settingRowBg{
    TTStyle* style = [TTTextStyle
                                          styleWithColor: [UIColor blueColor]
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
+(UIColor*) getColorForIndex:(int) index{
    if(index==0){
        return [UIColor redColor];
    }else if(index==1){
        return [UIColor blueColor];
    }else if(index==2){
        return [UIColor yellowColor];
    }
    else if(index==3){
        return [UIColor greenColor];
    }
    else if(index==4){
        return [UIColor cyanColor];
    }
    else if(index==5){
        return [UIColor orangeColor];
    }
    else if(index==6){
        return [UIColor lightGrayColor];
    }
    else if(index==7){
        return [UIColor brownColor];
    }else{
        return [UIColor blackColor];
    }
}
@end