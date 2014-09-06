//
//  KpiButton.m
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "KpiButton.h"

@implementation KpiButton
@synthesize kpi = _kpi;
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_kpi);
    [super dealloc];
}

@end
