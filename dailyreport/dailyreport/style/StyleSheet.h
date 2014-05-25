//
//  StylesSheet.h
//  hpsdf-ngp-mudole-iphone-client
//
//  Created by zhang wei on 11-11-22.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface StyleSheet : TTDefaultStyleSheet {
    
}

- (TTStyle*) configLabel;

+(UIColor*) colorFromHexString:(NSString*)hexString;
+(UIColor*) getColorForIndex:(int) index;
@end