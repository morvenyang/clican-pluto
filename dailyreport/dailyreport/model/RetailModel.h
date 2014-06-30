//
//  RetailModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "Retail.h"
#import "BaseModel.h"
@protocol RetailDelegate;

@interface RetailModel : BaseModel{
    NSString* _brand;
    id<RetailDelegate> _delegate;
}

@property(nonatomic,assign) id<RetailDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
- (void)load:(NSString*) type policy:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more;
@end

@protocol RetailDelegate <NSObject>

- (void) brandDidFinishLoad:(NSString*)dataProvider height:(int)height top:(int)top count:(int) count total:(long)total date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
