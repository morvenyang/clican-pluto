//
//  Video.h
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Video : NSObject{
    NSString* _title;
    NSString* _subtitle;
    NSString* _picUrl;
    NSString* _vid;
}

@property (nonatomic, copy) NSString* title;
@property (nonatomic, copy) NSString* subTitle;
@property (nonatomic, copy) NSString* picUrl;
@property (nonatomic, copy) NSString* vid;

@end
