//
//  VideoImageView.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoImageView.h"

@implementation VideoImageView

@synthesize actionUrl = _actionUrl;

-(void)dealloc{    
    [_actionUrl release];    
    [super dealloc];
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {   
    TTOpenURL(_actionUrl);
}

@end
