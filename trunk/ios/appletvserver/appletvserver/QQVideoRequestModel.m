//
//  QQVideoRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQVideoRequestModel.h"

@implementation QQVideoRequestModel

@synthesize delegate = _delegate;
@synthesize vid = _vid;

- (id)initWithVid:(NSNumber*)vid delegate:(id) delegate{
    if ((self = [super init])) {
        self.vid = vid;
        self.delegate = delegate;
    }
    return self;
}

@end
