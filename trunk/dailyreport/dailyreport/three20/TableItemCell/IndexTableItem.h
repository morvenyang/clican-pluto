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
    int _index;
    NSString* _brand;
}
@property(nonatomic,copy) NSString* backgroundImage;
@property(nonatomic,assign) int index;
@property(nonatomic,copy) NSString* brand;
+ (id)itemWithStyledText:(TTStyledText*)styledText backgroundImage:(NSString*)backgroundImage URL:(NSString*)url index:(int)index brand:(NSString*) brand;
@end
