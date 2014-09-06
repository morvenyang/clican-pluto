//
//  ProjectModel.h
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Project.h"
@protocol ProjectDelegate;

@interface ProjectModel : TTURLRequestModel{
    id<ProjectDelegate> _delegate;
}
@property(nonatomic,assign) id<ProjectDelegate> delegate;
- (void)loadProjects;
@end

@protocol ProjectDelegate <NSObject>

- (void)loadStart;
- (void)loadSuccess:(NSArray*) projects;
- (void)loadFailed:(NSError*) error message:(NSString*) message;

@end
