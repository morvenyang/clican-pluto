//
//  VideoImageView.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//


@interface VideoImageView : TTImageView{
    NSString* _actionUrl;
}

@property (nonatomic, copy) NSString* actionUrl;

@end
