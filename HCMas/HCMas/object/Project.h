//
//  Project.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Project : NSObject{
    NSNumber *_projectId;
    NSString *_projectName;
    BOOL _serverConfig;
    NSString* _serverUrl;
    NSArray *_kpis;
    NSArray *_kpiNames;
}
@property (nonatomic, retain) NSNumber* projectId;
@property (nonatomic, copy) NSString* projectName;
@property (nonatomic, assign) BOOL serverConfig;
@property (nonatomic, copy) NSString* serverUrl;
@property (nonatomic, retain) NSArray* kpis;
@property (nonatomic, retain) NSArray* kpiNames;
@end
