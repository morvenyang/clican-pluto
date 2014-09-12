//
//  Kpi.m
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "Kpi.h"

@implementation Kpi
@synthesize v1 =_v1;
@synthesize v2 =_v2;
@synthesize v3 =_v3;
@synthesize type = _type;
@synthesize dacTime = _dacTime;
@synthesize pointName = _pointName;
@synthesize alertGrade = _alertGrade;
@synthesize alertGrade_x = _alertGrade_x;
@synthesize alertGrade_y = _alertGrade_y;
@synthesize alertGrade_h = _alertGrade_h;
@synthesize dis_x = _dis_x;
@synthesize dis_h = _dis_h;
@synthesize dis_y = _dis_y;
@synthesize d2 = _d2;
@synthesize d3 = _d3;
-(id)init{
    self = [super init];
    if(self){
        return self;
    }
    return nil;
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_v1);
    TT_RELEASE_SAFELY(_v2);
    TT_RELEASE_SAFELY(_v3);
    TT_RELEASE_SAFELY(_type);
    TT_RELEASE_SAFELY(_dacTime);
    TT_RELEASE_SAFELY(_pointName);
    TT_RELEASE_SAFELY(_alertGrade);
    TT_RELEASE_SAFELY(_alertGrade_x);
    TT_RELEASE_SAFELY(_alertGrade_y);
    TT_RELEASE_SAFELY(_alertGrade_h);
    TT_RELEASE_SAFELY(_dis_x);
    TT_RELEASE_SAFELY(_dis_h);
    TT_RELEASE_SAFELY(_dis_y);
    TT_RELEASE_SAFELY(_d2);
    TT_RELEASE_SAFELY(_d3);
    [super dealloc];
}
@end
