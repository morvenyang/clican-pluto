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
@end