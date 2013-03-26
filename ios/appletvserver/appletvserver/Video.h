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
    NSString* _actors;
    NSString* _directors;
    NSString* _area;
    NSString* _score;
    NSString* _year;
    NSString* _description;
    NSString* _onSelect;
    NSMutableArray* _videoItemList;
}

@property (nonatomic, copy) NSString* title;
@property (nonatomic, copy) NSString* subTitle;
@property (nonatomic, copy) NSString* picUrl;
@property (nonatomic, copy) NSString* vid;

@property (nonatomic, copy) NSString* actors;
@property (nonatomic, copy) NSString* directors;
@property (nonatomic, copy) NSString* area;
@property (nonatomic, copy) NSString* score;
@property (nonatomic, copy) NSString* year;
@property (nonatomic, copy) NSString* description;
@property (nonatomic, copy) NSString* onSelect;
@property (nonatomic, retain) NSMutableArray* videoItemList;

@end
