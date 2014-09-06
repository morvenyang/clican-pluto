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
    [super dealloc];
}
@end
