//
//  IndexTableItem.h
//  dailyreport
//
//  Created by zhang wei on 14-5-19.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>

@interface IndexTableItem : TTTableStyledTextItem{
    NSString* _backgroundImage;
}
@property(nonatomic,copy) NSString* backgroundImage;

+ (id)itemWithStyledText:(TTStyledText*)styledText backgroundImage:(NSString*)backgroundImage URL:(NSString*)url;
@end
