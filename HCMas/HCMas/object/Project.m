//
//  Project.m
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "Project.h"

@implementation Project
@synthesize projectName = _projectName;
@synthesize projectId = _projectId;
@synthesize kpis = _kpis;
@synthesize kpiNames = _kpiNames;
@synthesize serverConfig = _serverConfig;
@synthesize serverUrl = _serverUrl;
-(id)init{
    self = [super init];
    if(self){
        return self;
    }
    return nil;
}
- (void) dealloc {
    TT_RELEASE_SAFELY(_projectId);
    TT_RELEASE_SAFELY(_serverUrl);
    TT_RELEASE_SAFELY(_projectName);
    TT_RELEASE_SAFELY(_kpis);
    TT_RELEASE_SAFELY(_kpiNames);
    [super dealloc];
}
@end
