//
//  DateButton.m
//  HCMas
//
//  Created by zhang wei on 14-9-10.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "DateButton.h"

@implementation DateButton

@synthesize type = _type;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_type);
    [super dealloc];
}
@end
