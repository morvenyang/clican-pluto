//
//  NoRetailModel.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BaseModel.h"
@protocol NoRetailDelegate;
@interface NoRetailModel : BaseModel{
    NSString* _brand;
    id<NoRetailDelegate> _delegate;
}
@property(nonatomic,assign) id<NoRetailDelegate> delegate;
@property (nonatomic, copy)     NSString*       brand;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate;
@end

@protocol NoRetailDelegate <NSObject>

- (void) brandDidFinishLoad:(NSArray*) noRetails date:(NSDate*) date;

- (void) brandDidStartLoad:(NSString*) brand;

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error;

@end
