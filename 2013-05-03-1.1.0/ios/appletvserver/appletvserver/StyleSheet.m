//
//  StylesSheet.h
//  hpsdf-ngp-mudole-iphone-client
//
//  Created by zhang wei on 11-11-22.
//  Copyright 2011年 __MyCompanyName__. All rights reserved.
//

#import "StyleSheet.h"

@implementation StyleSheet

- (TTStyle*) listTitle {
    UIColor* color = RGBCOLOR(51,51,51);
    return [TTTextStyle styleWithFont:[UIFont boldSystemFontOfSize:18] color:color textAlignment:UITextAlignmentCenter next:nil];
}

- (TTStyle*) assetTitle {
    UIColor* color =RGBCOLOR(153,153,153);
    return [TTTextStyle styleWithFont:[UIFont systemFontOfSize:16] color:color next:nil]; 
}

- (TTStyle*) assetDesc {
    UIColor* color =RGBCOLOR(102,102,102);
    return [TTTextStyle styleWithFont:[UIFont systemFontOfSize:12] color:color next:nil]; 
}

- (TTStyle*) categoryName {
    UIColor* color =RGBCOLOR(153,153,153);
    return [TTTextStyle styleWithFont:[UIFont systemFontOfSize:14] color:color next:nil]; 
}

- (TTStyle*) commentTitle {
    UIColor* color = RGBCOLOR(51,51,51);
    return [TTTextStyle styleWithFont:[UIFont systemFontOfSize:14] color:color textAlignment:UITextAlignmentLeft next:nil];
}

- (TTStyle*) commentAuthorAndDate {
    UIColor* color = RGBCOLOR(153,153,153);
    return [TTTextStyle styleWithFont:[UIFont systemFontOfSize:14] color:color textAlignment:UITextAlignmentLeft next:nil];
}

- (UIFont*) commentTitleFont {
    return [UIFont systemFontOfSize:14];
}

- (UIColor*) commentTitleColor {
    return RGBCOLOR(51,51,51);
}
- (UIColor*)toolbarButtonColorWithTintColor:(UIColor*)color forState:(UIControlState)state {
    return RGBCOLOR(200,200,200);
}

- (UIColor*)navigationBarTintColor {
    return RGBCOLOR(200,200,200);
}


- (UIColor*)toolbarButtonTextColorForState:(UIControlState)state {
    return RGBCOLOR(51,51,51);
}

- (TTStyle*)toolbarButton:(UIControlState)state {
    return [self toolbarButtonForState:state
                                 shape:[TTRoundedRectangleShape shapeWithRadius:4.5]
                             tintColor:TTSTYLEVAR(toolbarTintColor)
                                  font:nil];
}

- (UIColor*)searchBarTintColor {
    return [UIColor blackColor];
}

- (UIColor*)tablePlainBackgroundColor {
    return RGBCOLOR(39, 39, 39);
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (UIColor*)tablePlainCellSeparatorColor {
	return[UIColor clearColor];
}

- (UIColor*) searchTableSeparatorColor{
    return[UIColor clearColor];
}

- (UIColor*)tableGroupedCellSeparatorColor {
	return[UIColor clearColor];
}

- (UIColor*)assetDetailBackgroundColor {
	return RGBCOLOR(212, 212, 212);
}

- (TTStyle*)submitButton:(UIControlState)state {
    return [self toolbarButtonForState:state
                                 shape:[TTRoundedRectangleShape shapeWithRadius:4.5]
                             tintColor:TTSTYLEVAR(toolbarTintColor)
                                  font:nil];
}
@end